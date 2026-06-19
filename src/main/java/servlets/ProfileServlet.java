package servlets;

import dto.UserProfileDto;
import models.MemberShip;
import models.User;
import services.interfaces.FilesService;
import services.interfaces.MemberShipService;
import services.interfaces.UsersService;
import services.interfaces.WorkoutsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/profile")
@MultipartConfig
public class ProfileServlet extends HttpServlet {

    private UsersService usersService;
    private FilesService filesService;
    private MemberShipService memberShipService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String email = (String) req.getSession().getAttribute("userEmail");
            if (email != null) {
                UserProfileDto userProfileDto = usersService.getUserProfileByEmail(email);
                req.setAttribute("userProfile", userProfileDto);
                User user = usersService.getUserByEmail(email);
                List<MemberShip> memberships = memberShipService.getMembershipsByUser(user.getId());
                req.setAttribute("memberships", memberships);
            }
            req.getRequestDispatcher("/jsp/profile.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error loading user profile", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            String email = (String) req.getSession().getAttribute("userEmail");
            if (email == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
                return;
            }
            String updateProfile = "updateProfile";
            String uploadPhoto = "uploadPhoto";
            if (updateProfile.equals(action)) {
                UserProfileDto userProfileDto = UserProfileDto.builder()
                        .email(email)
                        .firstName(req.getParameter("firstName"))
                        .lastName(req.getParameter("lastName"))
                        .phone(req.getParameter("phone"))
                        .build();

                usersService.updateUserProfile(userProfileDto);
                resp.sendRedirect(req.getContextPath() + "/profile?success=profile");
            } else if (uploadPhoto.equals(action)) {
                Part part = req.getPart("profilePhoto");
                if (part != null && part.getSize() > 0) {
                    Long photoId = filesService.saveFileToStorageAndGetId(
                            part.getInputStream(),
                            part.getSubmittedFileName(),
                            part.getContentType(),
                            part.getSize()
                    );
                    usersService.updateUserPhoto(email, photoId);

                    resp.setContentType("application/json");
                    resp.getWriter().write("{\"status\":\"success\", \"photoId\":" + photoId + "}");
                } else {
                    resp.setContentType("application/json");
                    resp.getWriter().write("{\"status\":\"error\", \"message\":\"No file provided\"}");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        usersService = (UsersService) config.getServletContext().getAttribute("usersService");
        filesService = (FilesService) config.getServletContext().getAttribute("filesService");
        memberShipService = (MemberShipService) config.getServletContext().getAttribute("memberShipService");
    }
}
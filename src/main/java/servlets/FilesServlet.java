package servlets;

import models.FileInfo;
import services.interfaces.FilesService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/uploaded/files")
public class FilesServlet extends HttpServlet {

    private FilesService filesService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileId = req.getParameter("id");
        System.out.println("FilesServlet: requested file id = " + fileId);
        if (fileId == null || fileId.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "File ID is required");
            return;
        }

        FileInfo fileInfo = filesService.getFileInfo(Long.parseLong(fileId));
        resp.setContentType(fileInfo.getType());
        resp.setContentLength(fileInfo.getSize().intValue());
        resp.setHeader("Content-Disposition", "filename=\"" + fileInfo.getOriginalFileName());
        filesService.writeFileFromStorage(Long.parseLong(fileId), resp.getOutputStream());
        resp.flushBuffer();

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        filesService = (FilesService) config.getServletContext().getAttribute("filesService");
    }
}

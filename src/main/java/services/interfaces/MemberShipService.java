package services.interfaces;

import models.MemberShip;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface MemberShipService {

    void save(MemberShip memberShip) throws SQLException;

    MemberShip findById(Integer id) throws SQLException;

    List<MemberShip> getMembershipsByUser(UUID userId) throws SQLException;

    void updateMembership(MemberShip membership) throws SQLException;

    void deleteMembership(Integer id) throws SQLException;

    boolean hasUserMembershipForWorkout(UUID userId, Integer workoutId) throws SQLException;

    void incrementVisitCount(Integer membershipId) throws SQLException;
}

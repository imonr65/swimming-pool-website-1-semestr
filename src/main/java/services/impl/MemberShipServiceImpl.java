package services.impl;

import models.MemberShip;
import repository.interfaces.MemberShipRepository;
import services.interfaces.MemberShipService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MemberShipServiceImpl implements MemberShipService {

    private final MemberShipRepository memberShipRepository;


    public MemberShipServiceImpl(MemberShipRepository memberShipRepository) {
        this.memberShipRepository = memberShipRepository;
    }

    @Override
    public void save(MemberShip memberShip) throws SQLException {
        if (!hasUserMembershipForWorkout(memberShip.getUserId(), memberShip.getWorkoutId())) {
            memberShipRepository.save(memberShip);
        } else {
            throw new IllegalArgumentException("У пользователя уже есть абонемент на эту тренировку");
        }
    }

    @Override
    public MemberShip findById(Integer id) throws SQLException {
        return memberShipRepository.findById(id).orElse(null);
    }

    @Override
    public List<MemberShip> getMembershipsByUser(UUID userId) throws SQLException {
        return memberShipRepository.findByUserId(userId);
    }

    @Override
    public void updateMembership(MemberShip membership) throws SQLException {
        memberShipRepository.update(membership);
    }

    @Override
    public void deleteMembership(Integer id) throws SQLException {
        memberShipRepository.removeById(id);
    }

    @Override
    public boolean hasUserMembershipForWorkout(UUID userId, Integer workoutId) throws SQLException {
        return memberShipRepository.existsByUserAndWorkout(userId, workoutId);
    }

    @Override
    public void incrementVisitCount(Integer membershipId) throws SQLException {
        MemberShip memberShip = memberShipRepository.findById(membershipId).orElse(null);
        if (memberShip != null && memberShip.getCurrent_amount_visit() < memberShip.getTotal_visit()) {
            memberShip.setCurrent_amount_visit(memberShip.getCurrent_amount_visit() + 1);
            updateMembership(memberShip);
        } else {
            throw new IllegalStateException("Лимит посещений исчерпан или абонемент не найден");
        }
    }
}

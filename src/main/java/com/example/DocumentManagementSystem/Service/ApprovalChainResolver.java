package com.example.DocumentManagementSystem.Service;

import com.example.DocumentManagementSystem.entity.Department;
import com.example.DocumentManagementSystem.entity.Rank;
import com.example.DocumentManagementSystem.entity.User;
import com.example.DocumentManagementSystem.repository.RankRepository;
import com.example.DocumentManagementSystem.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component // @component => Servisler arası kullanılacak yardımcı bir bileşen olduğunu belirtiyoruz
public class ApprovalChainResolver {

    private final UserRepository userRepository;
    private final RankRepository rankRepository;

    public ApprovalChainResolver(UserRepository userRepository, RankRepository rankRepository) {
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
    }

       public Optional<User> findNextApprover(User currentActor) {

        Rank currentRank = currentActor.getRank();
        if (currentRank == null) {
            return Optional.empty();
        }

         int nextRutbe = currentRank.getRutbe() + 1;

          Optional<Rank> nextRank = rankRepository.findByRutbe(nextRutbe);

        if (nextRank.isEmpty()) {
              return Optional.empty();
        }

       Department currentDepartment = currentActor.getDepartment();
        return userRepository.findFirstByRankAndDepartment(nextRank.get(), currentDepartment);
    }
}
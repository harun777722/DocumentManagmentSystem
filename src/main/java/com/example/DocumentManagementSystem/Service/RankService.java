package com.example.DocumentManagementSystem.Service;

import com.example.DocumentManagementSystem.entity.Rank;
import com.example.DocumentManagementSystem.repository.RankRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Service
public class RankService {

     @Autowired
    private RankRepository rankRepository;


    public Rank creatRank(Rank rank){
        return rankRepository.save(rank);
    }

    public List<Rank> getAllRanks(){
        return rankRepository.findAll();
    }
    public Rank getRankById(@PathVariable("id") Long id){
        return rankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rütbe bulunamadı"));
    }
    public Rank updateRank(Long id, Rank rankDetails) {
        Rank existingRank = rankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sistemde böyle bir rütbe bulunamadı!"));

        // 2. Varsa, yeni gelen bilgilerle güncelliyoruz (rütbe dahil)
        existingRank.setName(rankDetails.getName());
        existingRank.setRutbe(rankDetails.getRutbe());

        // 3. Kaydedip geri dönüyoruz
        return rankRepository.save(existingRank);
    }

    public void deleteRank(Long id) {
        rankRepository.deleteById(id);
    }
}

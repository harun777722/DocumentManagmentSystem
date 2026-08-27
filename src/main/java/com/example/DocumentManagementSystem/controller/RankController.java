package com.example.DocumentManagementSystem.controller;

import com.example.DocumentManagementSystem.entity.Rank;
import com.example.DocumentManagementSystem.Service.RankService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;


@RestController
@RequestMapping("/api/ranks")
public class RankController {

    @Autowired
    private RankService rankService;



    @PostMapping
    public ResponseEntity<Rank> creatRank(@RequestBody Rank rank){
    return ResponseEntity.ok(rankService.creatRank(rank));
    }

    @GetMapping
    public ResponseEntity<List<Rank>> getAllRanks(){
        return ResponseEntity.ok(rankService.getAllRanks());
    }

    @GetMapping("/{id}")//buraya parametre koymamızın nedeni
    //Id ye göre çağırırken tekli alıyoruz kullanıcıyı bu yüzden bize bir ıd lazım
    //parametrede ıd ifadesini görünce sadece orada yazan ıd ye ait bilgiyi getirir
    public ResponseEntity<Rank> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(rankService.getRankById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Rank> updateRank(@PathVariable("id") Long id, @RequestBody Rank rankDetails) {
        return ResponseEntity.ok(rankService.updateRank(id, rankDetails));
    }

    // Rütbe Silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRank(@PathVariable("id") Long id) {
        rankService.deleteRank(id);
        return ResponseEntity.noContent().build();
    }
}

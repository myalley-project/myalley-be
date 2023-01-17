package com.myalley.blogReview.service;

import com.myalley.blogReview.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository repository;

    //public void changeLikeRepository()
}

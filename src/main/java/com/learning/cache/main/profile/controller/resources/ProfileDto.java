package com.learning.cache.main.profile.controller.resources;

public record ProfileDto(
      String userId,
      String name,
      Integer age,
      String hobby){}

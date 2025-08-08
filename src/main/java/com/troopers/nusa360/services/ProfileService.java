package com.troopers.nusa360.services;

import com.troopers.nusa360.models.Profile;
import com.troopers.nusa360.repositories.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final FileStorageService fileStorageService;

    public Profile updateAvatar(MultipartFile file, Long userId) {
        var profile = profileRepository.findById(userId).orElse(null);
        if (profile != null) {
            var avatarUrl = fileStorageService.storeUserFilePublic(file, userId);
            profile.setAvatar_url(avatarUrl);
            return profileRepository.save(profile);
        }
        return null;
    }

}

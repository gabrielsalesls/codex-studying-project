package dev.gabrielsales.bff_service.summary.api;

import dev.gabrielsales.bff_service.summary.UserSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserSummaryController {

    private final UserSummaryService userSummaryService;

    public UserSummaryController(UserSummaryService userSummaryService) {
        this.userSummaryService = userSummaryService;
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<UserSummaryResponse> findSummaryByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(userSummaryService.findByUserId(id));
    }
}

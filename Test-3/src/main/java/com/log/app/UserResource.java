package com.log.app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class UserResource {
    String id;
    String email;
    String nickname;
}
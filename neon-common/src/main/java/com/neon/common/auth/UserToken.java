package com.neon.common.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserToken {
    private int deviceId;
    private byte type;
    private int channeId;
    private short version;
    private int userId;
    private int time;
    private boolean ban;
}

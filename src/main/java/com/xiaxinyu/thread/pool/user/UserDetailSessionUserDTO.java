package com.xiaxinyu.thread.pool.user;


import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 会话用户信息DTO
 * @author LiuYang
 * @date 2020/7/7
 */
/*@Data
@NoArgsConstructor
@AllArgsConstructor*/
public class UserDetailSessionUserDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String loginName;
    @NotBlank
    private String email;
    @NotBlank
    private String realName;
    /**
     * 语言
     * <p>默认：zh_CN</p>
     */
    @Nullable
    private String language;
    /**
     * 账户是否锁定
     * true：锁定
     * <p>默认：false</p>
     */
    @Nullable
    private Boolean locked;
    /**
     * 账户是否启用
     * true：启用
     * <p>默认：true</p>
     */
    @Nullable
    private Boolean enabled;
    /**
     * 账户是否为管理员用户
     * true：是管理员
     * <p>默认：false</p>
     */
    @Nullable
    private Boolean admin;

    /**
     * 时区
     * <p>默认：CTT</p>
     */
    @Nullable
    private String timeZone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Nullable
    public String getLanguage() {
        return language;
    }

    public void setLanguage(@Nullable String language) {
        this.language = language;
    }

    @Nullable
    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(@Nullable Boolean locked) {
        this.locked = locked;
    }

    @Nullable
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(@Nullable Boolean enabled) {
        this.enabled = enabled;
    }

    @Nullable
    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(@Nullable Boolean admin) {
        this.admin = admin;
    }

    @Nullable
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(@Nullable String timeZone) {
        this.timeZone = timeZone;
    }
}

package com.baidu.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserInfo
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/10/15
 * @Version V1.09999999999999999
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private Integer id;

    private String username;
}
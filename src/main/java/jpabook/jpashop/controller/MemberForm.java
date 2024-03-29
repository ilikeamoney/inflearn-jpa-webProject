package jpabook.jpashop.controller;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.bridge.IMessage;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;


@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}

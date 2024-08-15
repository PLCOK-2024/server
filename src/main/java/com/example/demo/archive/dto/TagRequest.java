package com.example.demo.archive.dto;

import com.example.demo.common.rule.UniqueByAble;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class TagRequest implements UniqueByAble {
    @Size(min = 1, max = 50)
    private String name;

    @Override
    public Object getUniqueKey() {
        return name;
    }
}
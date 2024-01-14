package com.idealzouhu.reggie.dto;

import com.idealzouhu.reggie.entity.Setmeal;
import com.idealzouhu.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

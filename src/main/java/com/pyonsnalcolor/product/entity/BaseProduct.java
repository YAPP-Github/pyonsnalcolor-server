package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.enumtype.Category;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.StoreType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.Id;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Objects;

@SuperBuilder
@ToString
@Getter
@NoArgsConstructor
public abstract class BaseProduct extends BaseTimeEntity {
    @Id
    private String id;

    @Indexed
    private StoreType storeType;

    private String image;

    @Indexed
    private String name;

    private int price;

    private String description;

    private Category category;

    private EventType eventType;

    private Boolean isNew;

    public abstract ProductResponseDto convertToDto();

    public void updateIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String formattingPrice(int price) {
        return NumberFormat.getInstance().format(price);
    }

    public static Comparator<BaseProduct> getCategoryComparator() {
        return Comparator.comparing(p -> Category.GOODS.equals(p.getCategory()));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseProduct))
            return false;
        BaseProduct baseProduct = (BaseProduct) o;
        return this.name.equals(baseProduct.name) && this.storeType == baseProduct.storeType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, storeType);
    }
}
package otus.ware;

public class WareTypeMapper {

    // Convert Ware JPA Entity into WareDto
    public static WareTypeDto mapToWareTypeDto(WareType ware) {

        return new WareTypeDto(
                ware.getId(),
                ware.getWareName(),
                ware.getPrice()
        );
    }

    // Convert WareDto into Ware JPA Entity
    public static WareType mapToWareType(WareTypeDto wareDto) {
        return new WareType(
                wareDto.getId(),
                wareDto.getWareName(),
                wareDto.getPrice()
        );
    }
}


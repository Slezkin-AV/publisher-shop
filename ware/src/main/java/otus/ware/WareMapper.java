package otus.ware;

public class WareMapper {

    // Convert Ware JPA Entity into WareDto
    public static WareDto mapToWareDto(Ware ware) {

        return new WareDto(
                ware.getId(),
                ware.getWareType(),
                ware.getAmount(),
                ware.getWareName(),
                ware.getPrice()
        );
    }

    // Convert WareDto into Ware JPA Entity
    public static Ware mapToWare(WareDto wareDto) {
        return new Ware(
                wareDto.getId(),
                wareDto.getWareType(),
                wareDto.getAmount(),
                wareDto.getWareName(),
                wareDto.getPrice()
        );
    }
}


package common.async;

import com.polaris.cloud.client.AuditoriaClient;
import com.polaris.cloud.core.enums.Accion;
import common.types.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaInsertarTaskDto {

    private Entity<?> entity;
    private String modulo;
    private AuditoriaClient auditoriaClient;
    private Accion accion;
    private String clienteId;
    private String usuarioId;

}

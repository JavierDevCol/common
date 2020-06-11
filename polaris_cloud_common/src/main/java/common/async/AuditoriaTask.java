package common.async;

import com.polaris.cloud.core.domain.Auditoria;
import common.util.UtilJson;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class AuditoriaTask implements Runnable {

    private final AuditoriaInsertarTaskDto requestDto;

    public AuditoriaTask(AuditoriaInsertarTaskDto requestDto) {
        this.requestDto = requestDto;
    }

    @Override
    public void run() {
        try {
            Auditoria dto = new Auditoria();
            dto.setAccion(requestDto.getAccion());
            dto.setFechaInserccion(LocalDateTime.now());
            dto.setModulo(requestDto.getModulo());
            dto.setNombreClase(
                    requestDto.getEntity().getClass().getSimpleName()
            );
            dto.setObjeto(UtilJson.toString(requestDto.getEntity()));

            requestDto.getAuditoriaClient().insert(dto);
        }
        catch (Exception e) {
            log.warn("Error guardando auditoria.", e);
        }
    }

}

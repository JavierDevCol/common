package common.service.impl;

import com.polaris.cloud.client.AuditoriaClient;
import com.polaris.cloud.core.enums.Accion;
import common.async.AuditoriaInsertarTaskDto;
import common.async.AuditoriaTask;
import common.service.AuditoriaService;
import common.types.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.SchedulingTaskExecutor;

public class AuditoriaServiceImpl implements AuditoriaService {

    @Autowired(required = false)
    @Qualifier("threadPoolExecutor")
    private SchedulingTaskExecutor executor;

    @Autowired(required = false)
    private AuditoriaClient auditoriaClient;

    @Override
    public void auditar(Entity entity, String modulo, Accion accion) {
        AuditoriaInsertarTaskDto auditoriaInsertarTaskDto = new AuditoriaInsertarTaskDto();
        auditoriaInsertarTaskDto.setEntity(entity);
        auditoriaInsertarTaskDto.setModulo(modulo);
        auditoriaInsertarTaskDto.setAuditoriaClient(auditoriaClient);
        auditoriaInsertarTaskDto.setAccion(accion);
        executor.execute(new AuditoriaTask(auditoriaInsertarTaskDto));
    }
}

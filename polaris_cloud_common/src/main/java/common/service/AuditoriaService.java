package common.service;

import com.polaris.cloud.core.enums.Accion;
import common.types.Entity;

public interface AuditoriaService {

    void auditar(Entity entity, String modulo, Accion accion);


}

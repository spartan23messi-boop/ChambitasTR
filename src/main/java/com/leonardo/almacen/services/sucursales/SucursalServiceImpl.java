package com.leonardo.almacen.services.sucursales;

import com.leonardo.almacen.dto.sucursales.SucursalRequest;
import com.leonardo.almacen.dto.sucursales.SucursalResponse;
import com.leonardo.almacen.entities.Sucursal;
import com.leonardo.almacen.exceptions.ConflictoException;
import com.leonardo.almacen.exceptions.RecursoNoEncontradoException;
import com.leonardo.almacen.mappers.SucursalMapper;
import com.leonardo.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SucursalServiceImpl implements SucursalService{
    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {
        log.info("Listando todas las sucursales");
        return sucursalRepository.findAll().stream()
                .map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursal(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {
        log.info("Registrando nueva sucursal...");
        validarDatosUnicos(request);
        Sucursal sucursal =sucursalMapper.requestAEntidad(request);
        sucursalRepository.saveAndFlush(sucursal);
        log.info("Nueva sucursal registrada: {}", sucursal.getNombre());
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {
        log.info("Actualizando sucursal con id: {}",id);
        Sucursal sucursal=obtenerSucursal(id);
        validarCambiosUnicos(request, id);
        sucursal.actualizar(
                request.nombre(),
                request.direccion()
        );

        sucursalRepository.saveAndFlush(sucursal);

        log.info("Sucursal con id {} actualizada",id);

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando sucursal con id {}",id);
        Sucursal sucursal = obtenerSucursal(id);
        sucursalRepository.delete(sucursal);
        sucursalRepository.flush();
        log.info("Sucursal con id {} eliminada",id);
    }

    private Sucursal obtenerSucursal(Long id){
        log.info("Obteniendo sucursal {}",id);
        return sucursalRepository.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " + id));
    }

    private void validarDatosUnicos(SucursalRequest request){
        log.info("Validando nombre único...");
        if(sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException("Ya existe sucursal con el nombre de :"+request.nombre());
    }
    private void validarCambiosUnicos(SucursalRequest request, Long id){
        log.info("Validando cambio en nombre único...");
        if(sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(),id))
            throw new ConflictoException("Ya existe sucursal con el nombre de :"+request.nombre());
    }
}

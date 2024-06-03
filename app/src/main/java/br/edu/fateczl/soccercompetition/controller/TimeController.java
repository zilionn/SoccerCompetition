package br.edu.fateczl.soccercompetition.controller;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.soccercompetition.model.Time;
import br.edu.fateczl.soccercompetition.persistence.TimeDao;

public class TimeController implements IController<Time>{

    private final TimeDao tDao;

    public TimeController(TimeDao tDao){
        this.tDao = tDao;
    }

    @Override
    public void inserir(Time time) throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        tDao.insert(time);
        tDao.close();
    }

    @Override
    public void modificar(Time time) throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        tDao.update(time);
        tDao.close();
    }

    @Override
    public void deletar(Time time) throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        tDao.delete(time);
        tDao.close();
    }

    @Override
    public Time buscar(Time time) throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        return tDao.findOne(time);
    }

    @Override
    public List<Time> listar() throws SQLException {
        if(tDao.open() == null){
            tDao.open();
        }
        return tDao.findAll();
    }
}

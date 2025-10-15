package com.nbp.app.repository;

public interface IRemoteRepository {
    byte[] get(String url) throws Exception;
}
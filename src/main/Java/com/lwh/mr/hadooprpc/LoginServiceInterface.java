package com.lwh.mr.hadooprpc;


import org.apache.hadoop.ipc.VersionedProtocol;

public interface LoginServiceInterface extends VersionedProtocol {
	
	public static final long versionID=1L;
	public String login(String username, String password);

}

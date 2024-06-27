package com.example.travelappbackend.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SshTunnelingConfig {

    @Bean
    public Session sshSession() throws Exception {
        String sshHost = "210.89.190.191";
        String sshUser = "root";
        String sshPassword = "jwlee1228@^";
        int sshPort = 3000;

        String remoteHost = "localhost";
        int localPort = 3306;
        int remotePort = 3306;

        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        return session;
    }
}
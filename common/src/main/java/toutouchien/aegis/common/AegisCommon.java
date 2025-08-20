package toutouchien.aegis.common;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AegisCommon
{
    private final Logger logger;
    private final String ipLink;
    private Set<String> blockedIPs;

    public  AegisCommon(Logger logger)
    {
        this.logger = logger;
        this.ipLink = "https://raw.githubusercontent.com/pebblehost/hunter/master/ips.txt";
        this.blockedIPs = ConcurrentHashMap.newKeySet();
    }

    public boolean isBlocked(String ip)
    {
        return this.blockedIPs.contains(ip);
    }

    @Nullable
    public Set<String> fetchBotIPs()
    {
        Set<String> newBlockedIPs = ConcurrentHashMap.newKeySet();
        URL url;

        try {
            url = URI.create(this.ipLink).toURL();
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Failed to create URL from ipLink='" + this.ipLink + "'. Malformed URL.", e);
            return null;
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE,"Failed to create URI from ipLink='" + this.ipLink + "'. Invalid syntax.", e);
            return null;
        }

        try (InputStreamReader isr = new InputStreamReader(url.openStream());
             BufferedReader br = new BufferedReader(isr)) {
            String line;

            while ((line = br.readLine()) != null) {
                newBlockedIPs.add(line.trim());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "I/O error while fetching bot IPs from {" + url + "}.", e);
            return null;
        }

        return newBlockedIPs;
    }

    public void updateBlockedIPs(Set<String> newIps)
    {
        if (newIps != null)
        {
            this.blockedIPs = newIps;
        }
    }

    public  Set<String> getBlockedIPs()
    {
        return this.blockedIPs;
    }
}
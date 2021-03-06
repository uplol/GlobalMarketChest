package fr.epicanard.globalmarketchest.database.connections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.commons.lang3.tuple.Pair;

import fr.epicanard.globalmarketchest.GlobalMarketChest;
import fr.epicanard.globalmarketchest.exceptions.ConfigException;

public abstract class DatabaseConnection {
  protected Properties properties = new Properties();
  protected String host;
  protected String port;
  protected String database;
  protected String user;
  protected String password;

  public static String tableAuctions = "GMC_auctions";
  public static String tableShops = "GMC_shops";

  public static void configureTables() throws ConfigException {
    String prefix = GlobalMarketChest.plugin.getConfigLoader().getConfig().getString("Connection.TablePrefix");
    if (prefix == null)
      return;
    if (!prefix.matches("[a-zA-Z_]*"))
      throw new ConfigException("tablePrefix not containing only letters or/and _");

    DatabaseConnection.tableAuctions = prefix + "auctions";
    DatabaseConnection.tableShops = prefix + "shops";
  }

  protected String buildUrl() {
    return String.format("%s:%s/%s", this.host, this.port, this.database);
  }

  protected abstract Connection connect() throws ConfigException;

  protected abstract void disconnect(Connection connection);

  public abstract void recreateTables();

  public abstract Connection getConnection();

  public abstract void getBackConnection(Connection connection);

  public abstract void fillPool() throws ConfigException;

  public abstract void cleanPool();

  public abstract void closeRessources(ResultSet res, PreparedStatement prepared);

  public abstract void configFromConfigFile() throws ConfigException;

  public abstract String buildLimit(Pair<Integer, Integer> limit);
}

/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.kaaproject.kaa.server.appenders.cdap.config.gen;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class CdapConfig extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"CdapConfig\",\"namespace\":\"org.kaaproject.kaa.server.appenders.cdap.config.gen\",\"fields\":[{\"name\":\"stream\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"displayName\":\"Stream\",\"by_default\":\"stream\"},{\"name\":\"host\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"displayName\":\"Host\",\"by_default\":\"localhost\"},{\"name\":\"port\",\"type\":\"int\",\"displayName\":\"Port\",\"by_default\":10000},{\"name\":\"ssl\",\"type\":[\"boolean\",\"null\"],\"displayName\":\"Use SSL\"},{\"name\":\"verifySslCert\",\"type\":[\"boolean\",\"null\"],\"displayName\":\"Validate SSL Certificate\"},{\"name\":\"writerPoolSize\",\"type\":[\"int\",\"null\"],\"displayName\":\"Writer thread/connection pool size\"},{\"name\":\"callbackThreadPoolSize\",\"type\":[\"int\",\"null\"],\"displayName\":\"Callback thread pool size\",\"by_default\":2},{\"name\":\"version\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"displayName\":\"Version\"},{\"name\":\"authClient\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"displayName\":\"Authentication client class name\"},{\"name\":\"username\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"displayName\":\"Username\"},{\"name\":\"password\",\"type\":[{\"type\":\"string\",\"avro.java.string\":\"String\"},\"null\"],\"displayName\":\"Password\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
   private java.lang.String stream;
   private java.lang.String host;
   private int port;
   private java.lang.Boolean ssl;
   private java.lang.Boolean verifySslCert;
   private java.lang.Integer writerPoolSize;
   private java.lang.Integer callbackThreadPoolSize;
   private java.lang.String version;
   private java.lang.String authClient;
   private java.lang.String username;
   private java.lang.String password;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use {@link \#newBuilder()}. 
   */
  public CdapConfig() {}

  /**
   * All-args constructor.
   */
  public CdapConfig(java.lang.String stream, java.lang.String host, java.lang.Integer port, java.lang.Boolean ssl, java.lang.Boolean verifySslCert, java.lang.Integer writerPoolSize, java.lang.Integer callbackThreadPoolSize, java.lang.String version, java.lang.String authClient, java.lang.String username, java.lang.String password) {
    this.stream = stream;
    this.host = host;
    this.port = port;
    this.ssl = ssl;
    this.verifySslCert = verifySslCert;
    this.writerPoolSize = writerPoolSize;
    this.callbackThreadPoolSize = callbackThreadPoolSize;
    this.version = version;
    this.authClient = authClient;
    this.username = username;
    this.password = password;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return stream;
    case 1: return host;
    case 2: return port;
    case 3: return ssl;
    case 4: return verifySslCert;
    case 5: return writerPoolSize;
    case 6: return callbackThreadPoolSize;
    case 7: return version;
    case 8: return authClient;
    case 9: return username;
    case 10: return password;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: stream = (java.lang.String)value$; break;
    case 1: host = (java.lang.String)value$; break;
    case 2: port = (java.lang.Integer)value$; break;
    case 3: ssl = (java.lang.Boolean)value$; break;
    case 4: verifySslCert = (java.lang.Boolean)value$; break;
    case 5: writerPoolSize = (java.lang.Integer)value$; break;
    case 6: callbackThreadPoolSize = (java.lang.Integer)value$; break;
    case 7: version = (java.lang.String)value$; break;
    case 8: authClient = (java.lang.String)value$; break;
    case 9: username = (java.lang.String)value$; break;
    case 10: password = (java.lang.String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'stream' field.
   */
  public java.lang.String getStream() {
    return stream;
  }

  /**
   * Sets the value of the 'stream' field.
   * @param value the value to set.
   */
  public void setStream(java.lang.String value) {
    this.stream = value;
  }

  /**
   * Gets the value of the 'host' field.
   */
  public java.lang.String getHost() {
    return host;
  }

  /**
   * Sets the value of the 'host' field.
   * @param value the value to set.
   */
  public void setHost(java.lang.String value) {
    this.host = value;
  }

  /**
   * Gets the value of the 'port' field.
   */
  public java.lang.Integer getPort() {
    return port;
  }

  /**
   * Sets the value of the 'port' field.
   * @param value the value to set.
   */
  public void setPort(java.lang.Integer value) {
    this.port = value;
  }

  /**
   * Gets the value of the 'ssl' field.
   */
  public java.lang.Boolean getSsl() {
    return ssl;
  }

  /**
   * Sets the value of the 'ssl' field.
   * @param value the value to set.
   */
  public void setSsl(java.lang.Boolean value) {
    this.ssl = value;
  }

  /**
   * Gets the value of the 'verifySslCert' field.
   */
  public java.lang.Boolean getVerifySslCert() {
    return verifySslCert;
  }

  /**
   * Sets the value of the 'verifySslCert' field.
   * @param value the value to set.
   */
  public void setVerifySslCert(java.lang.Boolean value) {
    this.verifySslCert = value;
  }

  /**
   * Gets the value of the 'writerPoolSize' field.
   */
  public java.lang.Integer getWriterPoolSize() {
    return writerPoolSize;
  }

  /**
   * Sets the value of the 'writerPoolSize' field.
   * @param value the value to set.
   */
  public void setWriterPoolSize(java.lang.Integer value) {
    this.writerPoolSize = value;
  }

  /**
   * Gets the value of the 'callbackThreadPoolSize' field.
   */
  public java.lang.Integer getCallbackThreadPoolSize() {
    return callbackThreadPoolSize;
  }

  /**
   * Sets the value of the 'callbackThreadPoolSize' field.
   * @param value the value to set.
   */
  public void setCallbackThreadPoolSize(java.lang.Integer value) {
    this.callbackThreadPoolSize = value;
  }

  /**
   * Gets the value of the 'version' field.
   */
  public java.lang.String getVersion() {
    return version;
  }

  /**
   * Sets the value of the 'version' field.
   * @param value the value to set.
   */
  public void setVersion(java.lang.String value) {
    this.version = value;
  }

  /**
   * Gets the value of the 'authClient' field.
   */
  public java.lang.String getAuthClient() {
    return authClient;
  }

  /**
   * Sets the value of the 'authClient' field.
   * @param value the value to set.
   */
  public void setAuthClient(java.lang.String value) {
    this.authClient = value;
  }

  /**
   * Gets the value of the 'username' field.
   */
  public java.lang.String getUsername() {
    return username;
  }

  /**
   * Sets the value of the 'username' field.
   * @param value the value to set.
   */
  public void setUsername(java.lang.String value) {
    this.username = value;
  }

  /**
   * Gets the value of the 'password' field.
   */
  public java.lang.String getPassword() {
    return password;
  }

  /**
   * Sets the value of the 'password' field.
   * @param value the value to set.
   */
  public void setPassword(java.lang.String value) {
    this.password = value;
  }

  /** Creates a new CdapConfig RecordBuilder */
  public static org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder newBuilder() {
    return new org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder();
  }
  
  /** Creates a new CdapConfig RecordBuilder by copying an existing Builder */
  public static org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder newBuilder(org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder other) {
    return new org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder(other);
  }
  
  /** Creates a new CdapConfig RecordBuilder by copying an existing CdapConfig instance */
  public static org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder newBuilder(org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig other) {
    return new org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder(other);
  }
  
  /**
   * RecordBuilder for CdapConfig instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<CdapConfig>
    implements org.apache.avro.data.RecordBuilder<CdapConfig> {

    private java.lang.String stream;
    private java.lang.String host;
    private int port;
    private java.lang.Boolean ssl;
    private java.lang.Boolean verifySslCert;
    private java.lang.Integer writerPoolSize;
    private java.lang.Integer callbackThreadPoolSize;
    private java.lang.String version;
    private java.lang.String authClient;
    private java.lang.String username;
    private java.lang.String password;

    /** Creates a new Builder */
    private Builder() {
      super(org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.stream)) {
        this.stream = data().deepCopy(fields()[0].schema(), other.stream);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.host)) {
        this.host = data().deepCopy(fields()[1].schema(), other.host);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.port)) {
        this.port = data().deepCopy(fields()[2].schema(), other.port);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.ssl)) {
        this.ssl = data().deepCopy(fields()[3].schema(), other.ssl);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.verifySslCert)) {
        this.verifySslCert = data().deepCopy(fields()[4].schema(), other.verifySslCert);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.writerPoolSize)) {
        this.writerPoolSize = data().deepCopy(fields()[5].schema(), other.writerPoolSize);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.callbackThreadPoolSize)) {
        this.callbackThreadPoolSize = data().deepCopy(fields()[6].schema(), other.callbackThreadPoolSize);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.version)) {
        this.version = data().deepCopy(fields()[7].schema(), other.version);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.authClient)) {
        this.authClient = data().deepCopy(fields()[8].schema(), other.authClient);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.username)) {
        this.username = data().deepCopy(fields()[9].schema(), other.username);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.password)) {
        this.password = data().deepCopy(fields()[10].schema(), other.password);
        fieldSetFlags()[10] = true;
      }
    }
    
    /** Creates a Builder by copying an existing CdapConfig instance */
    private Builder(org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig other) {
            super(org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.SCHEMA$);
      if (isValidValue(fields()[0], other.stream)) {
        this.stream = data().deepCopy(fields()[0].schema(), other.stream);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.host)) {
        this.host = data().deepCopy(fields()[1].schema(), other.host);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.port)) {
        this.port = data().deepCopy(fields()[2].schema(), other.port);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.ssl)) {
        this.ssl = data().deepCopy(fields()[3].schema(), other.ssl);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.verifySslCert)) {
        this.verifySslCert = data().deepCopy(fields()[4].schema(), other.verifySslCert);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.writerPoolSize)) {
        this.writerPoolSize = data().deepCopy(fields()[5].schema(), other.writerPoolSize);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.callbackThreadPoolSize)) {
        this.callbackThreadPoolSize = data().deepCopy(fields()[6].schema(), other.callbackThreadPoolSize);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.version)) {
        this.version = data().deepCopy(fields()[7].schema(), other.version);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.authClient)) {
        this.authClient = data().deepCopy(fields()[8].schema(), other.authClient);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.username)) {
        this.username = data().deepCopy(fields()[9].schema(), other.username);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.password)) {
        this.password = data().deepCopy(fields()[10].schema(), other.password);
        fieldSetFlags()[10] = true;
      }
    }

    /** Gets the value of the 'stream' field */
    public java.lang.String getStream() {
      return stream;
    }
    
    /** Sets the value of the 'stream' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setStream(java.lang.String value) {
      validate(fields()[0], value);
      this.stream = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'stream' field has been set */
    public boolean hasStream() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'stream' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearStream() {
      stream = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'host' field */
    public java.lang.String getHost() {
      return host;
    }
    
    /** Sets the value of the 'host' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setHost(java.lang.String value) {
      validate(fields()[1], value);
      this.host = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'host' field has been set */
    public boolean hasHost() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'host' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearHost() {
      host = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'port' field */
    public java.lang.Integer getPort() {
      return port;
    }
    
    /** Sets the value of the 'port' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setPort(int value) {
      validate(fields()[2], value);
      this.port = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'port' field has been set */
    public boolean hasPort() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'port' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearPort() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'ssl' field */
    public java.lang.Boolean getSsl() {
      return ssl;
    }
    
    /** Sets the value of the 'ssl' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setSsl(java.lang.Boolean value) {
      validate(fields()[3], value);
      this.ssl = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'ssl' field has been set */
    public boolean hasSsl() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'ssl' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearSsl() {
      ssl = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'verifySslCert' field */
    public java.lang.Boolean getVerifySslCert() {
      return verifySslCert;
    }
    
    /** Sets the value of the 'verifySslCert' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setVerifySslCert(java.lang.Boolean value) {
      validate(fields()[4], value);
      this.verifySslCert = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'verifySslCert' field has been set */
    public boolean hasVerifySslCert() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'verifySslCert' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearVerifySslCert() {
      verifySslCert = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /** Gets the value of the 'writerPoolSize' field */
    public java.lang.Integer getWriterPoolSize() {
      return writerPoolSize;
    }
    
    /** Sets the value of the 'writerPoolSize' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setWriterPoolSize(java.lang.Integer value) {
      validate(fields()[5], value);
      this.writerPoolSize = value;
      fieldSetFlags()[5] = true;
      return this; 
    }
    
    /** Checks whether the 'writerPoolSize' field has been set */
    public boolean hasWriterPoolSize() {
      return fieldSetFlags()[5];
    }
    
    /** Clears the value of the 'writerPoolSize' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearWriterPoolSize() {
      writerPoolSize = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /** Gets the value of the 'callbackThreadPoolSize' field */
    public java.lang.Integer getCallbackThreadPoolSize() {
      return callbackThreadPoolSize;
    }
    
    /** Sets the value of the 'callbackThreadPoolSize' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setCallbackThreadPoolSize(java.lang.Integer value) {
      validate(fields()[6], value);
      this.callbackThreadPoolSize = value;
      fieldSetFlags()[6] = true;
      return this; 
    }
    
    /** Checks whether the 'callbackThreadPoolSize' field has been set */
    public boolean hasCallbackThreadPoolSize() {
      return fieldSetFlags()[6];
    }
    
    /** Clears the value of the 'callbackThreadPoolSize' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearCallbackThreadPoolSize() {
      callbackThreadPoolSize = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /** Gets the value of the 'version' field */
    public java.lang.String getVersion() {
      return version;
    }
    
    /** Sets the value of the 'version' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setVersion(java.lang.String value) {
      validate(fields()[7], value);
      this.version = value;
      fieldSetFlags()[7] = true;
      return this; 
    }
    
    /** Checks whether the 'version' field has been set */
    public boolean hasVersion() {
      return fieldSetFlags()[7];
    }
    
    /** Clears the value of the 'version' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearVersion() {
      version = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /** Gets the value of the 'authClient' field */
    public java.lang.String getAuthClient() {
      return authClient;
    }
    
    /** Sets the value of the 'authClient' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setAuthClient(java.lang.String value) {
      validate(fields()[8], value);
      this.authClient = value;
      fieldSetFlags()[8] = true;
      return this; 
    }
    
    /** Checks whether the 'authClient' field has been set */
    public boolean hasAuthClient() {
      return fieldSetFlags()[8];
    }
    
    /** Clears the value of the 'authClient' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearAuthClient() {
      authClient = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    /** Gets the value of the 'username' field */
    public java.lang.String getUsername() {
      return username;
    }
    
    /** Sets the value of the 'username' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setUsername(java.lang.String value) {
      validate(fields()[9], value);
      this.username = value;
      fieldSetFlags()[9] = true;
      return this; 
    }
    
    /** Checks whether the 'username' field has been set */
    public boolean hasUsername() {
      return fieldSetFlags()[9];
    }
    
    /** Clears the value of the 'username' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearUsername() {
      username = null;
      fieldSetFlags()[9] = false;
      return this;
    }

    /** Gets the value of the 'password' field */
    public java.lang.String getPassword() {
      return password;
    }
    
    /** Sets the value of the 'password' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder setPassword(java.lang.String value) {
      validate(fields()[10], value);
      this.password = value;
      fieldSetFlags()[10] = true;
      return this; 
    }
    
    /** Checks whether the 'password' field has been set */
    public boolean hasPassword() {
      return fieldSetFlags()[10];
    }
    
    /** Clears the value of the 'password' field */
    public org.kaaproject.kaa.server.appenders.cdap.config.gen.CdapConfig.Builder clearPassword() {
      password = null;
      fieldSetFlags()[10] = false;
      return this;
    }

    @Override
    public CdapConfig build() {
      try {
        CdapConfig record = new CdapConfig();
        record.stream = fieldSetFlags()[0] ? this.stream : (java.lang.String) defaultValue(fields()[0]);
        record.host = fieldSetFlags()[1] ? this.host : (java.lang.String) defaultValue(fields()[1]);
        record.port = fieldSetFlags()[2] ? this.port : (java.lang.Integer) defaultValue(fields()[2]);
        record.ssl = fieldSetFlags()[3] ? this.ssl : (java.lang.Boolean) defaultValue(fields()[3]);
        record.verifySslCert = fieldSetFlags()[4] ? this.verifySslCert : (java.lang.Boolean) defaultValue(fields()[4]);
        record.writerPoolSize = fieldSetFlags()[5] ? this.writerPoolSize : (java.lang.Integer) defaultValue(fields()[5]);
        record.callbackThreadPoolSize = fieldSetFlags()[6] ? this.callbackThreadPoolSize : (java.lang.Integer) defaultValue(fields()[6]);
        record.version = fieldSetFlags()[7] ? this.version : (java.lang.String) defaultValue(fields()[7]);
        record.authClient = fieldSetFlags()[8] ? this.authClient : (java.lang.String) defaultValue(fields()[8]);
        record.username = fieldSetFlags()[9] ? this.username : (java.lang.String) defaultValue(fields()[9]);
        record.password = fieldSetFlags()[10] ? this.password : (java.lang.String) defaultValue(fields()[10]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
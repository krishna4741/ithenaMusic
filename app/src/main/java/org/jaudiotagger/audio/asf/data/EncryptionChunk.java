package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;


public class EncryptionChunk extends Chunk {
    private String keyID;

    private String licenseURL;
    private String protectionType;
    private String secretData;

    private final ArrayList<String> strings;


    public EncryptionChunk(final BigInteger chunkLen) {
        super(GUID.GUID_CONTENT_ENCRYPTION, chunkLen);
        this.strings = new ArrayList<String>();
        this.secretData = "";
        this.protectionType = "";
        this.keyID = "";
        this.licenseURL = "";
    }


    public void addString(final String toAdd) {
        this.strings.add(toAdd);
    }


    public String getKeyID() {
        return this.keyID;
    }


    public String getLicenseURL() {
        return this.licenseURL;
    }


    public String getProtectionType() {
        return this.protectionType;
    }


    public String getSecretData() {
        return this.secretData;
    }


    public Collection<String> getStrings() {
        return new ArrayList<String>(this.strings);
    }


    @Override
    public String prettyPrint(final String prefix) {
        final StringBuilder result = new StringBuilder(super.prettyPrint(prefix));
        result.insert(0, Utils.LINE_SEPARATOR + prefix + " Encryption:"
                + Utils.LINE_SEPARATOR);
        result.append(prefix).append("	|->keyID ").append(this.keyID).append(
                Utils.LINE_SEPARATOR);
        result.append(prefix).append("	|->secretData ").append(this.secretData)
                .append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("	|->protectionType ").append(
                this.protectionType).append(Utils.LINE_SEPARATOR);
        result.append(prefix).append("	|->licenseURL ").append(this.licenseURL)
                .append(Utils.LINE_SEPARATOR);
        this.strings.iterator();
        for (final String string : this.strings) {
            result.append(prefix).append("   |->").append(string).append(Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }


    public void setKeyID(final String toAdd) {
        this.keyID = toAdd;
    }


    public void setLicenseURL(final String toAdd) {
        this.licenseURL = toAdd;
    }


    public void setProtectionType(final String toAdd) {
        this.protectionType = toAdd;
    }


    public void setSecretData(final String toAdd) {
        this.secretData = toAdd;
    }
}

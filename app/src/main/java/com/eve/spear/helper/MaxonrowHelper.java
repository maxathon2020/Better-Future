package com.eve.spear.helper;

import android.os.Build;

import com.mxw.Wallet;
import com.mxw.crypto.MnemonicUtils;
import com.mxw.networks.Network;
import com.mxw.protocol.http.HttpService;
import com.mxw.providers.JsonRpcProvider;
import com.mxw.utils.Base64s;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONObject;

import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

public class MaxonrowHelper {
    private static JsonRpcProvider jsonRpcProvider;

    public static JsonRpcProvider getRpcProvider() {
        if (jsonRpcProvider == null) {
            HttpService httpService = new HttpService("https://uatnet.usdp.io/", true);
            jsonRpcProvider = new JsonRpcProvider(httpService, new Network("uat", "uat"));
        }
        return jsonRpcProvider;
    }
    public static Wallet createWallet() {
        try {
            return Wallet.createNewWallet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Wallet createWallet(String mnemonic) {
        try {
            return Wallet.fromMnemonic(mnemonic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String computeSecret(Wallet wallet, final String publicKey) {
        try {
//        wallet.computeSharedSecret(wallet.getCompressedPublicKey());
            return wallet.computeSharedSecret(publicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getToken(Wallet wallet, String domain, String payload) {
        try {
            if (domain == null)
                return null;

            JSONObject signaturePayload = new JSONObject();
            signaturePayload.put("domain", domain);
            signaturePayload.put("timestamp", System.currentTimeMillis());
            signaturePayload.put("walletAddress", wallet.getAddress());

            if (payload != null)
                signaturePayload.put("payload", payload);

            String signature = wallet.signMessage(signaturePayload.toString().getBytes(), true);
            signaturePayload.put("signature", signature);
            return "Bearer " + Base64s.encode(signaturePayload.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(Wallet wallet, String payload) {
        try {
            return wallet.signMessage(payload.getBytes(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateMnemonic(int length) {
        return MnemonicUtils.generateMnemonic(generateRandomByte(length));
    }

    public static byte[] generateRandomByte(int length) {
        byte[] bytes = new byte[length];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                SecureRandom.getInstanceStrong().nextBytes(bytes);
                return bytes;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }


    public static Wallet decryptWallet(String encryptedJson, String password) {
        try {
            return Wallet.fromEncryptedJson(encryptedJson, password);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
}

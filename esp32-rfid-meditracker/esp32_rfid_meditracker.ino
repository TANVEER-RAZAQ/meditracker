#include <WiFi.h>
#include <HTTPClient.h>
#include <SPI.h>
#include <MFRC522.h>

// Pins for MFRC522 (adjust to your wiring)
#define SS_PIN 5
#define RST_PIN 27

const char* WIFI_SSID = "YOUR_WIFI_SSID";
const char* WIFI_PASS = "YOUR_WIFI_PASSWORD";

const char* BASE_URL = "http://<BACKEND_HOST>:8080"; // replace with your backend host/IP

MFRC522 rfid(SS_PIN, RST_PIN);

String uidToString(MFRC522::Uid *uid) {
  String out = "";
  for (byte i = 0; i < uid->size; i++) {
    if (uid->uidByte[i] < 0x10) out += "0";
    out += String(uid->uidByte[i], HEX);
  }
  out.toUpperCase();
  return out;
}

void postJson(const String& url, const String& payload) {
  HTTPClient http;
  http.begin(url);
  http.addHeader("Content-Type", "application/json");
  int code = http.POST(payload);
  Serial.printf("POST %s => %d\n", url.c_str(), code);
  String resp = http.getString();
  Serial.println(resp);
  http.end();
}

void setup() {
  Serial.begin(115200);
  SPI.begin();
  rfid.PCD_Init();

  WiFi.begin(WIFI_SSID, WIFI_PASS);
  Serial.print("Connecting to WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println(" connected");
}

void loop() {
  if (!rfid.PICC_IsNewCardPresent() || !rfid.PICC_ReadCardSerial()) {
    delay(200);
    return;
  }
  String uid = uidToString(&rfid.uid);
  Serial.printf("Card UID: %s\n", uid.c_str());

  // Example 1: Register or fetch patient
  String regUrl = String(BASE_URL) + "/api/registration";
  String regPayload = String("{\"rfidUid\":\"") + uid + "\",\"fullName\":\"Demo Patient\"}";
  postJson(regUrl, regPayload);

  // Example 2: Attempt payment of a billing item (replace BILLING_ID)
  // String payUrl = String(BASE_URL) + "/api/billing/pay";
  // String payPayload = String("{\"rfidUid\":\"") + uid + "\",\"billingId\":1}";
  // postJson(payUrl, payPayload);

  rfid.PICC_HaltA();
  rfid.PCD_StopCrypto1();

  delay(2000);
}

void setup() {
  pinMode(A0, OUTPUT);
  Serial.begin(9600); 
}

String input;
void loop() {
  if(Serial.available()){
    input = Serial.read();
    Serial.println(input);
    if(input.toInt()==49)
      digitalWrite(A0, HIGH);
    else if(input.toInt()==48)
      digitalWrite(A0, LOW);
  }
}

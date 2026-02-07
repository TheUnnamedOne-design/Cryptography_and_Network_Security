# Cryptography and Network Security

A comprehensive collection of cryptographic algorithm implementations in Java, featuring both classical and modern encryption techniques with client-server architecture for secure communication.

## 📋 Overview

This repository contains implementations of various cryptographic algorithms and encryption techniques, designed to demonstrate secure data transmission between clients and servers. Each implementation includes practical examples of encryption, decryption, and key management.

## 🔐 Implemented Algorithms

### Symmetric Key Cryptography
- **AES (Advanced Encryption Standard)** - Modern block cipher encryption
- **DES (Data Encryption Standard)** - Legacy block cipher encryption
- **SDES (Simplified DES)** - Educational simplified version of DES
- **IDEA (International Data Encryption Algorithm)** - Block cipher encryption

### Asymmetric Key Cryptography
- **RSA** - Public-key cryptosystem for secure data transmission
- **ElGamal** - Public-key encryption based on Diffie-Hellman

### Classical Ciphers
- **Caesar Cipher** - Simple substitution cipher with shift
- **Vigenere Cipher** - Polyalphabetic substitution cipher
- **PlayFair Cipher** - Digraph substitution cipher
- **Hill Cipher** - Polygraphic substitution using linear algebra
- **Rail Fence Cipher** - Transposition cipher technique
- **Row Column Cipher** - Columnar transposition cipher

### Utilities
- **Prime Generator** - Tool for generating prime numbers (useful for RSA and other algorithms)

## 🏗️ Project Structure

Each algorithm folder typically contains:
- `ClientEncryption.java` / `Client*.java` - Client-side implementation
- `ServerEncryption.java` / `Server*.java` - Server-side implementation
- Core algorithm implementation files
- Testing/Tester files for validation

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Basic understanding of cryptography concepts

### Running an Implementation

1. Navigate to the desired algorithm folder
2. Compile the Java files:
   ```bash
   javac *.java
   ```
3. Run the server first:
   ```bash
   java ServerEncryption
   ```
4. In a separate terminal, run the client:
   ```bash
   java ClientEncryption
   ```

## 📚 Learning Objectives

This repository demonstrates:
- Implementation of various encryption and decryption algorithms
- Client-server communication with encrypted data
- Key generation and management
- Differences between symmetric and asymmetric encryption
- Classical vs. modern cryptographic techniques

## ⚠️ Disclaimer

These implementations are for educational purposes only. For production environments, use well-tested cryptographic libraries and follow security best practices.

## 📝 License

This project is available for educational and learning purposes.

## 🤝 Contributing

Feel free to fork this repository and add more cryptographic algorithms or improve existing implementations.

# 📘 Vardiya Defteri - Backend

Bu proje, fabrikalardaki vardiya notlarını dijital olarak tutmak için geliştirilmiş bir sistemin backend tarafını içerir.

## 🚀 Teknolojiler

- Java 17
- Spring Boot 3
- Spring Security + JWT
- PostgreSQL
- Maven
- RESTful API

## 🔐 Özellikler

- Sicil ve şifre ile kimlik doğrulama (JWT token)
- Rol bazlı yetkilendirme (SİSTEM_YÖNETİCİSİ, İŞLETME_SORUMLUSU, NORMAL_KULLANICI)
- Hatalı girişte bloke etme (parametrik)
- Kullanıcı yönetimi (soft delete, bloke kaldırma)
- Vardiya notu ekleme, listeleme, filtreleme, güncelleme
- PDF ve Excel rapor oluşturma
- Güncellenmiş notları ✏️ işareti ile gösterme

## ⚙️ Kurulum

1. PostgreSQL çalışıyor olmalı
2. `application.properties` dosyasına veritabanı bilgilerini girin
3. IntelliJ IDEA ile projeyi açın ve çalıştırın:

```bash
mvn spring-boot:run

by Serhat

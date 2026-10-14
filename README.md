# 🎓 AI Study Assistant Platform  

An AI-powered learning platform that helps students study smarter by analyzing uploaded materials, generating summaries, quizzes, and enabling real-time voice interaction with AI.

---

## 🚀 Features

### 📂 Smart File-Based Learning
- Upload study materials (PDF, DOCX, etc.)
- AI analyzes file content
- Automatically generates:
  - Structured summaries
  - Practice quizzes
  - Key knowledge extraction

### 🎙 Voice AI Interaction (vAPI Integration)
- Real-time voice communication with AI
- Students can:
  - Ask questions verbally
  - Receive spoken explanations
  - Practice interactive learning

### 🤖 OpenAI Integration
- Document summarization
- Quiz generation
- Context-aware Q&A support
- AI-powered study assistant

### ☁ File & Email Management
- File handling via GitHub Repository
- Email notifications using Google Email Service:
  - Account verification
  - Study updates
  - Payment confirmation

### 💳 Payment Integration (VNPay)
- Secure payment gateway
- Premium subscription packages
- Transaction verification handling

---

## 🏗 System Architecture

```
User
 ├── Upload Study File
 │      └── Backend Processing
 │              ├── OpenAI API (Summary + Quiz Generation)
 │              ├── Database Storage
 │              └── Voice Interaction via vAPI
 │
 └── Upgrade to Premium
        └── VNPay Payment Gateway
                └── Payment Verification
```

---

## 🛠 Tech Stack

### Backend
- Spring Boot
- RESTful APIs

### AI Services
- OpenAI API
- vAPI (Voice AI)

### Database
- MySQL

### External Services
- GitHub Repository (File Management)
- Google Email Service
- VNPay Payment Gateway

---

## ⚙ Installation

```bash
# Clone repository
git clone https://github.com/phamminhhiepcoder/Nutgrow.git

# Run project
Click Run Java in VS Code or Intellij IDEA
```

---

## 🔐 Environment Variables

Setup your keys in application.properties:

```
openai.api.key=<open-ai-key>
openai.model=<open-ai-model>
spring.security.oauth2.client.registration.google.client-id=<client-id>
spring.security.oauth2.client.registration.google.client-secret=<client-secret>
github.token=<github-token>
```

---

## Screenshots
<img width="1861" height="956" alt="image" src="https://github.com/user-attachments/assets/fb4ee99c-8026-4396-9d0a-afc1e2b30e53" />
<img width="1608" height="619" alt="image" src="https://github.com/user-attachments/assets/bc3870c4-5d14-4fe8-9dd1-a4d18b255546" />
<img width="1868" height="894" alt="image" src="https://github.com/user-attachments/assets/aa325769-85be-48a1-9111-62053ebd53a2" />
<img width="1761" height="855" alt="image" src="https://github.com/user-attachments/assets/acae4969-3170-4975-b5bc-eb349a88051d" />

---

## 📌 Future Improvements
- AI performance optimization
- Multi-language support
- Mobile app integration


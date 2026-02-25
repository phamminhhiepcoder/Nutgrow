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
- Node.js / Spring Boot
- RESTful APIs

### AI Services
- OpenAI API
- vAPI (Voice AI)

### Database
- MySQL / PostgreSQL

### External Services
- GitHub Repository (File Management)
- Google Email Service
- VNPay Payment Gateway

---

## ⚙ Installation

```bash
# Clone repository
git clone https://github.com/your-username/ai-study-assistant.git

# Install dependencies
npm install

# Run project
npm start
```

---

## 🔐 Environment Variables

Create a `.env` file:

```
OPENAI_API_KEY=your_openai_key
VAPI_API_KEY=your_vapi_key
VNPAY_TMN_CODE=your_vnpay_code
VNPAY_HASH_SECRET=your_secret
EMAIL_USER=your_google_email
EMAIL_PASS=your_google_app_password
```

---

## 📌 Future Improvements
- AI performance optimization
- Multi-language support
- Advanced analytics dashboard
- Mobile app integration

---

## 👨‍💻 Author
Developed by [Your Name]

---

## 📄 License
This project is licensed under the MIT License.

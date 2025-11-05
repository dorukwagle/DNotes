# 🗒️ DNotes — Secure, Structured, and Beautiful Local Notes App

**DNotes** is a high-performance, fully offline notes application built with **JavaFX**.  
It combines **rich markdown editing**, **deep organizational structure**, and **AES-256 encryption** — all while remaining **blazingly fast** and **self-contained** with no cloud dependency.

---

## ✨ Key Highlights

### 🧠 Rich Markdown/Text Editing
Powered by [RichTextFX](https://github.com/FXMisc/RichTextFX), DNotes provides a rich, structured editing experience with advanced formatting and shortcuts.

**Formatting Features:**
- **Bold** (`Ctrl + B`)
- *Italic* (`Ctrl + I`)
- _Underline_ (`Ctrl + U`)
- ~~Strikethrough~~
- **Headings**:
  - `Ctrl + 1` → H1
  - `Ctrl + 2` → H2
  - `Ctrl + 3` → H3
  - `Ctrl + 4` → H4
- **Blockquotes**, **Ordered/Unordered Lists**
- **Checkbox lists** with blur effect for checked items.
- **Clickable Links**
- **Font color** and **text background highlights**
- **Automatic paragraph and style preservation**

**Custom Shortcuts:**
| Shortcut | Action |
|-----------|---------|
| `Ctrl + B` | Toggle Bold |
| `Ctrl + I` | Toggle Italic |
| `Ctrl + U` | Toggle Underline |
| `Ctrl + 1–4` | Apply Heading (H1–H4) |
| `Ctrl + Q` | Add Quick Note |
| `Ctrl + K` | Open Context Menu |

> All shortcuts are bound dynamically after the editor is attached to a visible `Scene` to ensure correct lifecycle initialization.

---

## 🧩 Note Organization

DNotes offers a **hierarchical storage structure** for intuitive organization:

- **Collections** → contain multiple **Books**  
- **Books** → contain multiple **Pages (Notes)**  
- Each page is stored as a file in the local filesystem.

You can switch seamlessly between collections and books, providing a notebook-like experience with fast access and structured navigation.

---

## 🗑️ Trash and Recovery System

- Deleted notes are **not immediately removed** — they move to the **Trash**.
- Notes can be **restored** at any time from Trash.
- Prevents accidental deletion and data loss.

---

## 🔐 Lock & Encryption

- You can **Lock** DNotes with a password.
- Locking enables **AES-256 encryption** for all note data and database files.
- Notes are decrypted only in memory when unlocked.
- Uses **ChaCha20-Poly1305** for modern cryptography and integrity verification.
- Non-encrypted mode still uses **lightweight XOR obfuscation** to prevent casual snooping.

> Passwords are **never stored in plaintext** — only salted hashes are stored and verified during unlock.

---

## 🗂️ Sharing, Backup, and Restore

### 🔄 Share Notes
- You can **export individual notes** as shareable `.dnote` files.
- Shared notes can be **imported** on any DNotes installation.
- Data remains secure and encoded using the app’s obfuscation layer.

### 💾 Backup Entire Library
The app supports full backup and restore using a **binary-structured local file**.

#### Backup Process (`BackupWriter`)
- Writes:
  - Metadata (`MetaWriter`)
  - Encrypted/Obfuscated data streams
  - Database file
  - All notes (name + content)
- Encodes data with **continuation-length encoding** for efficient storage.
- Optionally encrypts all content using the user’s password.

#### Restore Process (`BackupReader`)
- Reads backup files sequentially:
  - Validates metadata and password
  - Restores database and notes
  - Verifies integrity markers and byte lengths

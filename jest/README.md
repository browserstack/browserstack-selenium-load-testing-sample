# browserstack-selenium-load-testing-sample

![BrowserStack Logo](https://d98b8t1nnulk5.cloudfront.net/production/images/layout/logo-header.png?1469004780)

## Getting Started

### Run Sample Build

1. **Clone the repository**

   ```sh
   git clone https://github.com/browserstack/browserstack-selenium-load-testing-sample.git
   cd browserstack-selenium-load-testing-sample
   cd jest
   ```

2. **Install Node dependencies**

   ```sh
   npm install
   ```

3. **Install BrowserStack CLI**

   Download the appropriate BrowserStack CLI binary based on your operating system:

   - **macOS x86**  
     [browserstack-cli-macOS-x86](https://load-api.browserstack.com/api/v1/binary?os=macos&arch=x64)

   - **macOS ARM**  
     [browserstack-cli-macOS-arm](https://load-api.browserstack.com/api/v1/binary?os=macos&arch=arm64)

   - **Windows x86**  
     [browserstack-cli-windows](https://load-api.browserstack.com/api/v1/binary?os=win&arch=x64)

   - **Linux x86**  
     [browserstack-cli-linux-x86](https://load-api.browserstack.com/api/v1/binary?os=linux&arch=x64)

   - **Linux ARM**  
     [browserstack-cli-linux-arm](https://load-api.browserstack.com/api/v1/binary?os=linux&arch=arm64)

   > The download is a zip. Extract it and place the `browserstack-cli` binary in the root of your project.

4. **Verify the BrowserStack CLI**

   Before running the binary, check that it is signed by BrowserStack:

   - **macOS**: the output must include `TeamIdentifier=YQ5FZQ855D` (Browserstack Inc).

     ```sh
     codesign --verify --strict ./browserstack-cli && codesign -dv ./browserstack-cli 2>&1 | grep TeamIdentifier
     ```

   - **Windows** (PowerShell): `Status` must be `Valid` and the signer must be BrowserStack.

     ```powershell
     Get-AuthenticodeSignature .\browserstack-cli.exe | Format-List Status, SignerCertificate
     ```

   If the check fails, do not run the binary.

5. **Set your credentials**

   The BrowserStack CLI reads your credentials from environment variables. Copy your username and access key from [Account Settings](https://www.browserstack.com/accounts/settings) and export them in the shell you run the test from:

   ```sh
   # macOS / Linux (bash or zsh)
   export BROWSERSTACK_USERNAME="<your-username>"
   # Type the key at the prompt: it is not echoed, and it stays out of your shell history
   printf 'BrowserStack access key: '; read -rs BROWSERSTACK_ACCESS_KEY; echo
   export BROWSERSTACK_ACCESS_KEY
   ```

   ```powershell
   # Windows (PowerShell)
   $env:BROWSERSTACK_USERNAME = "<your-username>"
   # Type the key at the prompt: it is not echoed, and it stays out of your PowerShell history
   $env:BROWSERSTACK_ACCESS_KEY = [System.Net.NetworkCredential]::new('', (Read-Host -AsSecureString 'BrowserStack access key')).Password
   ```

   These variables last only for the current terminal session. If the CLI reports an authentication error, check that both are set in the terminal you run it from.

   **Never commit your credentials.** Leave the `userName` and `accessKey` placeholders in `browserstack-load.yml` as they are (the environment variables override them), and keep your access key out of every file you push. If a key is ever exposed, regenerate it from Account Settings.

6. **Run tests using BrowserStack CLI**

   ```sh
   # macOS / Linux
   ./browserstack-cli load run
   ```

   ```powershell
   # Windows (PowerShell)
   .\browserstack-cli.exe load run
   ```

7. **View Test Results**

   Visit the [BrowserStack Load-Testing Dashboard](https://load.browserstack.com/projects) to monitor and analyze your test runs.

---

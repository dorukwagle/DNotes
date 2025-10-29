# Maintainer: Your Name <your.email@example.com>
pkgname=dnotes
pkgver=1.0.0
pkgrel=1
pkgdesc="A note-taking application"
arch=('x86_64')
license=('MIT') # Or your license
depends=('java-runtime-headless') # Your app has its own JRE, but this is good practice

# The source is the app-image created by Maven
source=("$pkgname-$pkgver.tar.gz") # You'd package your target/dist/DNotes folder into this tarball
sha256sums=('SKIP') # For local builds, you can skip checksum

package() {
  # Create the installation directory
  install -d "${pkgdir}/opt/${pkgname}"
  
  # Copy the entire contents of your app-image into the package
  cp -r "${srcdir}/${pkgname}-${pkgver}/"* "${pkgdir}/opt/${pkgname}/"
  
  # Create a symlink so the user can run 'dnotes' from the terminal
  install -d "${pkgdir}/usr/bin"
  ln -s "/opt/${pkgname}/bin/DNotes" "${pkgdir}/usr/bin/${pkgname}"
  
  # Install the .desktop file for the applications menu
  install -Dm644 "${srcdir}/dnotes.desktop" "${pkgdir}/usr/share/applications/dnotes.desktop"
  
  # Install the icon
  install -Dm644 "${srcdir}/dnotes.png" "${pkgdir}/usr/share/icons/hicolor/128x128/apps/dnotes.png"
}

SUMMARY = "Library for rendering SVG files"
DESCRIPTION = "A small library to render Scalable Vector Graphics (SVG), \
associated with the GNOME Project. It renders SVG files to Cairo surfaces. \
Cairo is the 2D, antialiased drawing library that GNOME uses to draw things to \
the screen or to generate output for printing."
HOMEPAGE = "https://gitlab.gnome.org/GNOME/librsvg"
BUGTRACKER = "https://gitlab.gnome.org/GNOME/librsvg/issues"

LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=4fbd65380cdd255951079008b364516c"

SECTION = "x11/utils"
DEPENDS = "cairo gdk-pixbuf glib-2.0 libxml2 pango python3-docutils-native"
BBCLASSEXTEND = "native nativesdk"

GNOMEBASEBUILDCLASS = "autotools"
inherit cargo_common gnomebase pixbufcache gobject-introspection gettext rust vala

SRC_URI += " \
    file://0001-Makefile.am-pass-rust-target-to-cargo-also-when-not-.patch \
"
SRC_URI[archive.sha256sum] = "fffb61b08cd5282aaae147a02b305166a7426fad22a8b9427708f0f2fc426ebc"

UPSTREAM_CHECK_REGEX = "librsvg-(?P<pver>\d+\.\d+\.(?!9\d+)\d+)"

BASEDEPENDS:append = " cargo-native"

CARGO_DISABLE_BITBAKE_VENDORING = "1"

export RUST_BACKTRACE = "full"
export RUSTFLAGS
export RUST_TARGET = "${RUST_HOST_SYS}"

RUSTFLAGS:append:mips = " --cfg crossbeam_no_atomic_64"
RUSTFLAGS:append:mipsel = " --cfg crossbeam_no_atomic_64"
RUSTFLAGS:append:powerpc = " --cfg crossbeam_no_atomic_64"
RUSTFLAGS:append:riscv32 = " --cfg crossbeam_no_atomic_64"

do_configure[postfuncs] += "cargo_common_do_configure"

inherit rust-target-config

do_compile:prepend() {
    export LD_LIBRARY_PATH="${STAGING_LIBDIR_NATIVE}"
    install -m 0644 ${STAGING_LIBDIR_NATIVE}/rustlib/${BUILD_SYS}.json ${RUST_TARGETS_DIR}/${BUILD_SYS}.json
    sed -ie 's,"linker": ".*","linker": "${RUST_TARGET_CC}",g' ${RUST_TARGETS_DIR}/${RUST_HOST_SYS}.json
}

CVE_STATUS[CVE-2018-1000041] = "not-applicable-platform: Issue only applies on Windows"

CACHED_CONFIGUREVARS = "ac_cv_path_GDK_PIXBUF_QUERYLOADERS=${STAGING_LIBDIR_NATIVE}/gdk-pixbuf-2.0/gdk-pixbuf-query-loaders"

PACKAGECONFIG ??= "gdkpixbuf"
PACKAGECONFIG:append:class-target = " ${@bb.utils.contains('GI_DATA_ENABLED', 'True', 'vala', '', d)}"
PACKAGECONFIG[gdkpixbuf] = "--enable-pixbuf-loader,--disable-pixbuf-loader,gdk-pixbuf-native"
PACKAGECONFIG[vala] = "--enable-vala,--disable-vala"

do_install:append() {
    rm -f ${D}${libdir}/gdk-pixbuf-2.0/*/loaders/*.a ${D}${libdir}/gdk-pixbuf-2.0/*/loaders/*.la
}

PACKAGES =+ "librsvg-gtk rsvg"
FILES:rsvg = "${bindir}/rsvg* \
              ${datadir}/pixmaps/svg-viewer.svg \
              ${datadir}/themes"
FILES:librsvg-gtk = "${libdir}/gdk-pixbuf-2.0/*/*/*.so \
                     ${datadir}/thumbnailers/librsvg.thumbnailer"
RRECOMMENDS:librsvg-gtk = "gdk-pixbuf-bin"

PIXBUF_PACKAGES = "librsvg-gtk"

SUMMARY = "cargo applet to build and install C-ABI compatible dynamic and static libraries."
HOMEPAGE = "https://crates.io/crates/cargo-c"
LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=384ed0e2e0b2dac094e51fbf93fdcbe0 \
"

SRC_URI = "crate://crates.io/cargo-c/${PV};name=cargo-c \
           file://0001-parking-lot-Use-libc-SYS_futex_time64-on-riscv32.patch;patchdir=../parking_lot_core-0.9.8/ \
"
SRC_URI[cargo-c.sha256sum] = "f83d9e98867fecb774b2f45cd7ed9e253dc453b50a90774f3b8ffecefe095cf7"
S = "${CARGO_VENDORING_DIRECTORY}/cargo-c-${PV}"

DEBUG_PREFIX_MAP += "-ffile-prefix-map=${CARGO_HOME}=${TARGET_DBGSRC_DIR}"

inherit cargo cargo-update-recipe-crates pkgconfig

DEPENDS = "openssl curl"

require ${BPN}-crates.inc

CARGO_BUILD_FLAGS:class-native = "-v --frozen --target ${BUILD_SYS} ${BUILD_MODE} --manifest-path=${CARGO_MANIFEST_PATH}"
CARGO_TARGET_SUBDIR:class-native = "${BUILD_SYS}/${BUILD_DIR}"

BBCLASSEXTEND = "native"

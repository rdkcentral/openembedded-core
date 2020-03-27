SUMMARY = "Device Tree Compiler"
HOMEPAGE = "https://devicetree.org/"
DESCRIPTION = "The Device Tree Compiler is a tool used to manipulate the Open-Firmware-like device tree used by PowerPC kernels."
SECTION = "bootloader"
LICENSE = "GPLv2 | BSD"
DEPENDS = "flex-native bison-native"

<<<<<<< HEAD:meta/recipes-kernel/dtc/dtc.inc
SRC_URI = "git://git.kernel.org/pub/scm/utils/dtc/dtc.git \
           file://make_install_1.5.1.patch \
           file://0001-dtc-Fix-Makefile-to-add-CFLAGS-not-override.patch \
=======
SRC_URI = "git://git.kernel.org/pub/scm/utils/dtc/dtc.git \
           file://make_install_1.5.1.patch \
>>>>>>> BCOM-4454: [Yocto 3.1] Use the same DTC V1.4.1 as Morty build:meta/recipes-kernel/dtc/dtc_1.5.1.inc
           "

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\d+(\.\d+)+)"

EXTRA_OEMAKE='NO_PYTHON=1 PREFIX="${prefix}" LIBDIR="${libdir}" DESTDIR="${D}"'

inherit pkgconfig

S = "${WORKDIR}/git"

do_install () {
	oe_runmake install
}

PACKAGES =+ "${PN}-misc"
FILES_${PN}-misc = "${bindir}/convert-dtsv0 ${bindir}/ftdump ${bindir}/dtdiff"

RDEPENDS_${PN}-misc += "bash diffutils"

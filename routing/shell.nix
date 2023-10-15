with import <nixpkgs> {};
mkShell {
	buildInputs = [
		(python310.withPackages(ps: with ps; [ psycopg2 requests ]))
	];
	NIX_LD_LIBRARY_PATH = lib.makeLibraryPath [
		stdenv.cc.cc
		openssl
	];
	NIX_LD = lib.fileContents "${stdenv.cc}/nix-support/dynamic-linker";
}


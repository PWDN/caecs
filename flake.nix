{
  description = "PROO_LAB_template";
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };
  outputs = {self, nixpkgs, flake-utils, ...}: flake-utils.lib.eachDefaultSystem(system: 
    let
      pkgname = "PROO_LAB_template";
      pkgs = import nixpkgs {
        inherit system;
      };
    in {
      packages = {
        default = pkgs.callPackage(
          {
            stdenv,
            lib,
          }: stdenv.mkDerivation {
            name = pkgname;
            src = ./.;
            buildInputs = with pkgs; [
              pkg-config
              cmake
              git
            ];
            installPhase = ''
              mkdir -p $out/bin
              cp -r ./${pkgname}  $out/bin/
            '';
          }
        ) {};
      };
      devShell = pkgs.mkShell {
        buildInputs = with pkgs; [
          cmake
          git
          adoptopenjdk-bin
          gradle
          jdt-language-server
          tree
          glfw-wayland
          wayland
          libglvnd # For libEGL
          vulkan-loader
          glxinfo
          vulkan-tools vulkan-headers vulkan-validation-layers
          systemd # For libudev
          seatd # For libseat
          libxkbcommon
          libinput
          mesa # For libgbm
          fontconfig
          stdenv.cc.cc.lib
        ];
      };        
    }
  );
}


#!/bin/bash
# Copyright (c) Facebook, Inc. and its affiliates.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Minimal setup for Ubuntu 20.04.
set -eufx -o pipefail

# Run the velox setup script first.
source "$(dirname "${BASH_SOURCE}")/../velox/scripts/setup-ubuntu.sh"
SUDO="${SUDO:-"sudo --preserve-env"}"

function install_proxygen {
  # proxygen requires python and gperf
  ${SUDO} apt update
  ${SUDO} apt install -y gperf python3
  github_checkout facebook/proxygen "${FB_OS_VERSION}" --depth 1
  # Folly Portability.h being used to decide whether or not support coroutines
  # causes issues (build, lin) if the selection is not consistent across users of folly.
  EXTRA_PKG_CXXFLAGS=" -DFOLLY_CFG_NO_COROUTINES"
  cmake_install -DBUILD_TESTS=OFF
}

function install_presto_deps {
  run_and_time install_proxygen
}

if [[ $# -ne 0 ]]; then
  for cmd in "$@"; do
    run_and_time "${cmd}"
  done
  echo "All specified dependencies installed!"
else
  if [ "${INSTALL_PREREQUISITES:-Y}" == "Y" ]; then
    echo "Installing build dependencies"
    run_and_time install_build_prerequisites
  else
    echo "Skipping installation of build dependencies since INSTALL_PREREQUISITES is not set"
  fi
  install_velox_deps
  install_presto_deps
  echo "All dependencies for Prestissimo installed!"
fi

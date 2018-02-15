# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/xenial64"
  config.vm.provider "virtualbox" do |vb|
    # Customize the amount of memory on the VM:
    vb.memory = "2048"
    vb.cpus = 2
  end

  config.vm.provision "shell", inline: <<-SHELL
    apt-get update -qq
    apt-get install -yqq software-properties-common \
                         build-essential \
                         cmake \
                         cmake-data \
                         cmake-extras \
                         git \
                         maven
    add-apt-repository ppa:webupd8team/java -y
    apt-get update -qq
    echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
    apt-get install -yqq oracle-java8-installer oracle-java8-set-default 
    bash /etc/profile.d/jdk.sh
    apt-get install -yqq ocl-icd-libopencl1 \
                         opencl-headers \
                         clinfo \
                         ocl-icd-opencl-dev \
                         beignet-opencl-icd \
                         mesa-opencl-icd \
                         opencl-headers
    apt-get install libgl1-mesa-dev
    cd /tmp
    git clone https://github.com/gpu/JOCL
    git clone https://github.com/gpu/JOCLCommon
    ln -s /usr/lib/jvm/java-8-oracle /usr/lib/jvm/java
    cmake JOCL
    sudo make
    cd JOCL
    mvn clean package -DskipTests
  SHELL
end

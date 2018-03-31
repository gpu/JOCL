Vagrant.configure("2") do |config|
  config.vm.box = "centos/7"
  config.vm.provider "virtualbox" do |vb|
    # Customize the amount of memory on the VM:
    vb.memory = "2048"
    vb.cpus = 2
  end


config.vm.provision "shell", inline: <<-SHELL
curl http://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm >epel-release-latest-7.noarch.rpm
rpm -ivh epel-release-latest-7.noarch.rpm

yum install -y cmake3 gcc-c++ git maven mesa-libGL-devel ocl-icd-devel 
cd /tmp
git clone https://github.com/gpu/JOCL
git clone https://github.com/gpu/JOCLCommon
export JAVA_HOME=/etc/alternatives/java_sdk
cmake3 JOCL
make
cd JOCL
mvn clean package -DskipTests
SHELL
end
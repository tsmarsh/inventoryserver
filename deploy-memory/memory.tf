# Specify the provider and access details
provider "aws" {
  region = "${var.aws_region}"
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
}

# Create a VPC to launch our instances into
resource "aws_vpc" "default" {
  cidr_block = "10.0.0.0/16"
}

# Create an internet gateway to give our subnet access to the outside world
resource "aws_internet_gateway" "default" {
  vpc_id = "${aws_vpc.default.id}"
}

# Grant the VPC internet access on its main route table
resource "aws_route" "internet_access" {
  route_table_id         = "${aws_vpc.default.main_route_table_id}"
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = "${aws_internet_gateway.default.id}"
}

# Create a subnet to launch our instances into
resource "aws_subnet" "default" {
  vpc_id                  = "${aws_vpc.default.id}"
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
}

# A security group for the ELB so it is accessible via the web
resource "aws_security_group" "elb" {
  name        = "inventory-${var.server_type}-elb"
  description = "Used in the terraform"
  vpc_id      = "${aws_vpc.default.id}"

  ingress {
    from_port   = 5555
    to_port     = 5555
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "default" {
  name        = "inventory_${var.server_type}_sg"
  description = "Used in the terraform"
  vpc_id      = "${aws_vpc.default.id}"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 5555
    to_port     = 5555
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_elb" "web" {
  name = "inventory-${var.server_type}-elb"

  subnets         = ["${aws_subnet.default.id}"]
  security_groups = ["${aws_security_group.elb.id}"]
  instances       = ["${aws_instance.web.id}"]

  listener {
    instance_port     = 5555
    instance_protocol = "http"
    lb_port           = 5555
    lb_protocol       = "http"
  }
}

resource "aws_key_pair" "auth" {
  key_name   = "${var.key_name}"
  public_key = "${file(var.public_key_path)}"
}

resource "aws_instance" "web" {
  instance_type = "t2.micro"
  ami = "ami-4dd18837"

  key_name = "${aws_key_pair.auth.id}"
  vpc_security_group_ids = ["${aws_security_group.default.id}"]

  subnet_id = "${aws_subnet.default.id}"
}

resource "null_resource" "instance_provisioner" {
  connection {
    type = "ssh"
    user = "ec2-user"
    host = "${aws_instance.web.public_ip}"
    private_key = "${file(var.private_key_path)}"
  }

  provisioner "file" {
    source = "../${var.server_type}/target/${var.server_type}-1.0-SNAPSHOT.jar"
    destination = "/home/ec2-user/${var.server_type}.jar"
  }

  provisioner "file" {
    destination = "/tmp/inventory.service"
    content = <<EOF
      [Unit]
      Description=InventoryServer
      [Service]
      Environment="INV_HOST=${aws_elb.web.dns_name}"
      ExecStart=/usr/bin/java -jar /home/ec2-user/${var.server_type}.jar
      User=ec2-user
      Type=simple
      [Install]
      WantedBy=multi-user.target
      EOF
  }

  provisioner "remote-exec" {
    inline = [
      "sudo mv /tmp/inventory.service /usr/lib/systemd/system/inventory.service",
      "sudo yum -y update",
      "sudo yum -y install java-1.8.0-openjdk",
      "sudo systemctl enable inventory",
      "sudo systemctl start inventory"
    ]
  }
}

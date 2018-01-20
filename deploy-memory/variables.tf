variable "aws_access_key" {
  default = "AKIAIJMA42VRNRQLBXAQ"
}

variable "aws_secret_key" {
  default = "HllzX0K5LA/ff8VzO5OPNr4DV0pUqBSVQUIWnSe/"
}

variable "aws_region" {
  default = "us-east-1"
}

variable "key_name" {
  default = "inventory"
}

variable "server_type" {
  default = "memory"
}

variable "public_key_path" {
  default = "~/.ssh/id_rsa.pub"
}

variable "private_key_path" {
  default = "~/.ssh/id_rsa"
}

variable "instance_size" {
  default = "t2.micro"
}

output "ip" {
  value = "${aws_elb.web.dns_name}"
}
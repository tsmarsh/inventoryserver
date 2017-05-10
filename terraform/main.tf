provider "aws" {
  region = "us-east-1"
}

variable env {
  type = string
  description = "suffix for names, e.g. 'prod'"
}

resource "aws_ecr_repository" "docker_repo" {
  name = "invserver-${env}"
}

resource "aws_ecs_cluster" "cluster" {
  name = "invserver-${env}"

}

resource "aws_ecs_task_definition"{

  container_definitions = ""
  family = ""
}
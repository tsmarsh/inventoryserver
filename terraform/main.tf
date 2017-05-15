resource "aws_ecr_repository" "docker_repo" {
  name = "memserver"
}

resource "aws_ecs_cluster" "memserver" {
  name = "memserver-${var.env}"
}

data "aws_ecs_task_definition" "memserver" {
  task_definition = "${aws_ecs_task_definition.memserver.family}"
}


resource "aws_ecs_task_definition" "memserver" {
  family = "memserver"

  container_definitions = <<DEFINITION
[
  {
    "cpu": 128,
    "essential": true,
    "image": "memserver:${var.env}",
    "memory": 256,
    "memoryReservation": 256,
    "name": "memserver",
    "portMappings": [
        {
          "hostPort": 80,
          "containerPort": 1414,
          "protocol": "tcp"
        }
    ]
  }
]
DEFINITION
}

resource "aws_ecs_service" "memserver" {
  name = "memserver"
  cluster = "${aws_ecs_cluster.memserver.id}"
  desired_count = 2

  # Track the latest ACTIVE revision
  task_definition = "${aws_ecs_task_definition.memserver.family}:${max("${aws_ecs_task_definition.memserver.revision}", "${data.aws_ecs_task_definition.memserver.revision}")}"
}
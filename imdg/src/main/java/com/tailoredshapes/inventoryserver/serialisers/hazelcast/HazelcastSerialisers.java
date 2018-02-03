package com.tailoredshapes.inventoryserver.serialisers.hazelcast;


import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import com.tailoredshapes.inventoryserver.model.*;

import java.io.IOException;

public interface HazelcastSerialisers {
    static int typeId(Object name){
        return Math.abs(name.hashCode());
    }

    StreamSerializer<Category> categorySerialiser = new StreamSerializer<Category>() {
        @Override
        public int getTypeId() {
            return typeId("Category");
        }

        @Override
        public void destroy() {

        }

        @Override
        public void write(ObjectDataOutput out, Category category) throws IOException {
            out.writeLong(category.getId());
            out.writeUTF(category.getName());
            out.writeUTF(category.getFullname());
            out.writeObject(category.getParent());
        }

        @Override
        public Category read(ObjectDataInput in) throws IOException {
            return new Category()
                    .setId(in.readLong())
                    .setName(in.readUTF())
                    .setFullname(in.readUTF())
                    .setParent(in.readObject());
        }

    };

    StreamSerializer<Metric> metricSerialiser = new StreamSerializer<Metric>() {
        @Override
        public int getTypeId() {
            return typeId("Metric");
        }

        @Override
        public void destroy() {

        }

        @Override
        public void write(ObjectDataOutput out, Metric metric) throws IOException {
            out.writeLong(metric.getId());
            out.writeUTF(metric.getValue());
            out.writeObject(metric.getType());
        }

        @Override
        public Metric read(ObjectDataInput in) throws IOException {
            return new Metric()
                    .setId(in.readLong())
                    .setValue(in.readUTF())
                    .setType(in.readObject());
        }
    };


    StreamSerializer<MetricType> metricTypeSerialiser = new StreamSerializer<MetricType>() {
        @Override
        public void write(ObjectDataOutput out, MetricType metricType) throws IOException {
            out.writeLong(metricType.getId());
            out.writeUTF(metricType.getName());
        }

        @Override
        public MetricType read(ObjectDataInput in) throws IOException {
            return new MetricType()
                    .setId(in.readLong())
                    .setName(in.readUTF());
        }

        @Override
        public int getTypeId() {
            return typeId("MetricType");
        }

        @Override
        public void destroy() {

        }
    };

    StreamSerializer<Inventory> inventorySerializer = new StreamSerializer<Inventory>() {
        @Override
        public void write(ObjectDataOutput out, Inventory inventory) throws IOException {
            out.writeLong(inventory.getId());
            out.writeObject(inventory.getCategory());
            out.writeObject(inventory.getParent());
            out.writeObject(inventory.getMetrics());
        }

        @Override
        public Inventory read(ObjectDataInput in) throws IOException {
            return new Inventory()
                    .setId(in.readLong())
                    .setCategory(in.readObject())
                    .setParent(in.readObject())
                    .setMetrics(in.readObject());
        }

        @Override
        public int getTypeId() {
            return typeId("Inventory");
        }

        @Override
        public void destroy() {

        }
    };

    StreamSerializer<User> userSerializer = new StreamSerializer<User>() {
        @Override
        public void write(ObjectDataOutput out, User user) throws IOException {
            out.writeLong(user.getId());
            out.writeObject(user.getName());
            out.writeObject(user.getCreated());
            out.writeObject(user.getInventories());
        }

        @Override
        public User read(ObjectDataInput in) throws IOException {
            return new User()
                    .setId(in.readLong())
                    .setName(in.readObject())
                    .setCreated(in.readObject())
                    .setInventories(in.readObject());
        }

        @Override
        public int getTypeId() {
            return typeId("User");
        }

        @Override
        public void destroy() {

        }
    };
}
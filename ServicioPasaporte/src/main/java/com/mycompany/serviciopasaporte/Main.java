
package com.mycompany.serviciopasaporte;

import java.util.Scanner;

/* 
Jenny Ximena Ordoñez Espinosa - Realizó el procesamiento de inputs
Jhon Sebastián Moreno Triana - Juntó todas las clases existentes y orquestó el correcto funcionamiento entre ellas.
José Simón Ramos Sandoval - Modificó el heap para que cumpliera con los requerimientos del problema.
Angie Sofía Cárdenas Sánchez - Diseñó el heap para implementarlo.
Juan David Guarnizo Gutierrez - Implementó el árbol AVL.
Juan Felipe Hincapié Gómez -  Sugirió la idea para la simulación del servicio de pasaportes.
 */

public class Main {
    public static class DynamicArray<T> {
        private T[] array;
        private int size;
        private int capacity;


        public DynamicArray(){
            this.capacity = 2; 
            this.size = 0;
            this.array =(T[]) new Object[capacity];
        }

        public int size(){
            return this.size;
        }

        public void add(T key){
            if(this.size == this.capacity){
               this.resize();
            }
            this.array[this.size] = key;
            this.size++;
        }

        private void resize(){
            this.capacity*=2;
            T[] newArray = (T[]) new Object[this.capacity];
            for(int i = 0; i<size;i++){
                newArray[i] = this.array[i];
            }
            this.array = newArray;
        }

        public T get(int i){
            return this.array[i];
        }

        public void set(int index, T key){
            while(index>=capacity){
                size = index;
                resize();  
            }
            this.array[index] = key;
        }

        public void remove(int index){
            for(int i = index; i < size-1; i++){
                this.array[i]=this.array[i+1];
            }
            this.size--;
        }    
    }
    
    public static class MinHeap<T> {
        public int size = 0;
        private DynamicArray<Integer> heap = new DynamicArray<Integer>();
        private DynamicArray<T> data = new DynamicArray<T>();
        
        public MinHeap(){
            heap.add(null);
            data.add(null);
        }

        public int parent(int i){
            return (int) Math.floor(i/2);
        }

        public int leftChild(int i){
            return 2*i;
        }

        public int rightChild(int i){
            return 2*i+1;
        }

        private void siftUp(int i){
            while( i>1 && heap.get(parent(i))>heap.get(i)){
                Integer temp = heap.get(i);
                T tempData = data.get(i);

                heap.set(i,heap.get(parent(i)));
                heap.set(parent(i),temp);

                data.set(i,data.get(parent(i)));
                data.set(parent(i),tempData);

                i = parent(i);
            }
        }

        private void siftDown(int i){
            int maxIndex = i;
            Integer l = leftChild(i), r = rightChild(i);
            if(l<=size && heap.get(l)<heap.get(maxIndex)){
                maxIndex = l;
            }
            if(r<=size && heap.get(r)<heap.get(maxIndex)){
                maxIndex = r;
            }
            if(i!=maxIndex){
                Integer temp = heap.get(i);
                T tempData = data.get(i);

                heap.set(i,heap.get(maxIndex));
                heap.set(maxIndex,temp);

                data.set(i,data.get(maxIndex));
                data.set(maxIndex,tempData);

                siftDown(maxIndex);
            }  
        }

        public void insert(int p, T item){
            size++;

            heap.set(size,p);
            data.set(size,item);
            
            siftUp(size);
        }

        public int extractMax(){
            int result = heap.get(1);
            heap.set(1,heap.get(size));
            data.set(1,data.get(size));
            size--;
            siftDown(1);
            return result;
        }

        public T extractMaxData(){
            T result = data.get(1);
            heap.set(1,heap.get(size));
            data.set(1,data.get(size));
            size--;
            siftDown(1);
            return result;
        }

        public void remove(int i){
            heap.set(i, Integer.MIN_VALUE);
            siftUp(i);
            extractMax();
        }

        public void changePriority(int i, int p){
            int oldP = heap.get(i);

            heap.set(i,p);
            if(p<oldP){
                siftUp(i);
            }else{
                siftDown(i);
            }
        }
        
        public void cleanHeap(){
            size = 0;
        }
        
        public int get(int i){
            return heap.get(i);
        }
        
        public T getData(int i){
            return data.get(i);
        }
    }
    
    public static class Node<T> {
        T data;
        Node next;

        public Node(T item){
            this.data = item;
            this.next = null;
        }
    }
    
    public static class Queue<T> {
        private Node<T> front, rear;

        public Queue(){
            this.front=null;
            this.rear=null;
        }

        public void enqueue(T item){
            Node newNode = new Node(item);
            if(this.rear!=null){
                this.rear.next=newNode;
                this.rear=this.rear.next;}
            else{
                this.front = newNode;
                this.rear = this.front;
            }
        }

        public T dequeue(){
            if(!isEmpty()){
                T d = this.front.data;
                if(this.front==this.rear){
                    this.rear=null;
                }
                this.front = this.front.next;
                return d;
            }else{
                System.out.println("Empty queue.");
                return null;
            }

        }

        public boolean isEmpty(){
            return this.front == null;
        }

        public void printQueue(){
            while(!this.isEmpty()){
                System.out.println(this.dequeue());
            }
        }
    }
    
    public static class InputListener{
        private Scanner sc;
        private Queue<String> queue = new Queue<String>();
        
        public int numOfInstructions;
        
        public InputListener(int num, Scanner scanner){
            numOfInstructions = num;
            sc = scanner;
            read();
        }
        
        public void read(){
            sc.nextLine();
            for(int i = 0; i<numOfInstructions; i++ ){
                queue.enqueue(sc.nextLine());
            }
        }
        
        public Queue<String> getQueue(){
            return queue;
        }
        
        public int getNum(){
            return numOfInstructions;
        }
    }
    
    public static class Gestor{
        private int numOfCases;
        private Queue<Queue<String>> cases;
        private Scanner sc = new Scanner(System.in);
        private MinHeap<String> heap = new MinHeap<String>();
        private int[] casesNums;
        
        public Gestor(){
            saveCases();
        }
        
        public void saveCases(){
            int numOfCases = sc.nextInt();
            cases = new Queue<Queue<String>>();
            casesNums = new int[numOfCases];
            for(int j = 0; j<numOfCases; j++){
                int num = sc.nextInt();
                InputListener input = new InputListener(num,sc);
                cases.enqueue(input.getQueue());
                casesNums[j] = num;
            }
        }
        
        public void run(){
            int num = 0;
            while(!cases.isEmpty()){
                Queue<String> q = cases.dequeue();
                while(!q.isEmpty()){
                    String event = q.dequeue();
                    if(event.equals("atiende")){
                        readHeap();
                        heap.cleanHeap();
                    }else{
                        int priority;
                        event = event.replace("entra ","");
                        if(event.equals("discapacitado")){
                            priority = 0;
                        }else if(event.equals("conBebe")){
                            priority = 1;
                        }else if(event.equals("embarazada")){
                            priority = 2;
                        }else if(event.equals("mayor")){
                            priority = 3;
                        }else{
                            priority = 4;
                        }
                        heap.insert(priority*casesNums[num]+heap.size-1, event);
                    }
                }
                num++;
                heap.cleanHeap();
            }
        }
        
        private void readHeap(){
            while(heap.size!=0){
                System.out.println(heap.extractMaxData());
            }
        }
        
        public void read(){
            while(!cases.isEmpty()){
                Queue<String> q = cases.dequeue();
                q.printQueue();
            }
        }
    }
    
    public static void main(String[] args) {
        Gestor gestor = new Gestor();
        gestor.run();
    }
}
